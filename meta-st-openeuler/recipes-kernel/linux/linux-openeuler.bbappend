FILESEXTRAPATHS:append := "${THISDIR}/linux-openeuler/${LINUX_VERSION}:"

COMPATIBLE_MACHINE = "stm32mpcommon"

# for external device tree
inherit external-dt

DEPENDS += "rsync-native"

# fetch kernel and patch for myir-st-kernel
OPENEULER_LOCAL_NAME = "myir-st-kernel-${PV}"
OPENEULER_REPO_NAMES = "myir-st-kernel-${PV}"

# remove origin kernel and patch in linux-openeuler.inc
SRC_URI:remove = " \
    file://kernel-${PV} \
    file://patches/${ARCH}/0001-arm64-add-zImage-support-for-arm64.patch \
    file://patches/${ARCH}/0001-kernel6.6-arm64-add-zImage-support-for-arm64.patch \
"

# myir st kernel's baseline is 6.6.48
# because of do_kernel_version_sanity_check, here set PV = 6.6.48
PV = "6.6.48"

KERNEL_CONFIG_FRAGMENTS += "${@bb.utils.contains('KBUILD_DEFCONFIG', 'defconfig', ' file://fragment-01-cleanup.cfg ', '', d)}"
KERNEL_CONFIG_FRAGMENTS += "${@bb.utils.contains('KBUILD_DEFCONFIG', 'defconfig', ' file://fragment-02-addons.cfg ', '', d)}"
KERNEL_CONFIG_FRAGMENTS += " ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'file://fragment-03-systemd.cfg', '', d)} "
KERNEL_CONFIG_FRAGMENTS += " file://fragment-04-modules.cfg "
KERNEL_CONFIG_FRAGMENTS += " ${@oe.utils.ifelse(d.getVar('KERNEL_SIGN_ENABLE') == '1', 'file://optional-fragment-05-signature.cfg','')} "
KERNEL_CONFIG_FRAGMENTS += " ${@bb.utils.contains('MACHINE_FEATURES', 'nosmp', ' file://optional-fragment-06-nosmp.cfg ', '', d)} "
KERNEL_CONFIG_FRAGMENTS += " ${@bb.utils.contains('MACHINE_FEATURES', 'efi', 'file://optional-fragment-07-efi.cfg ', '', d)} "


# kernel config adaptation for openeuler
KERNEL_CONFIG_FRAGMENTS += " file://openeuler-adapt.cfg "


SRC_URI:append = " \
    file://myir-st-kernel-${PV} \
    ${KERNEL_CONFIG_FRAGMENTS} \
"

S = "${WORKDIR}/myir-st-kernel-${PV}"

# ----------------------------
# defconfig configuration
# no external defconfig
# no out tree defconfig
OPENEULER_KERNEL_CONFIG = ""
# in tree defconfig
KBUILD_DEFCONFIG = "defconfig"

#---------------------------------------------------------------
# Module kernel signature
KERNEL_SIGN_ENABLE ?= "0"
EXTRA_OEMAKE += "${@oe.utils.ifelse(d.getVar('KERNEL_SIGN_ENABLE') == '1', 'INSTALL_MOD_STRIP=1','')}"
EXTRA_OEMAKE += "${@bb.utils.contains('EXTERNAL_DT_ENABLED', '1', 'KBUILD_EXTDTS=${STAGING_EXTDT_DIR}/${EXTDT_DIR_LINUX}', '', d)}"


correct_buildpaths() {
    FILES=" \
    ${B}/drivers/video/logo/logo_linux_clut224.c \
    ${B}/drivers/tty/vt/consolemap_deftbl.c \
    ${B}/lib/oid_registry_data.c \
    "
    for f in $FILES; do
        if [ -e $f ]; then
            sed -i "s|${TMPDIR}||g" $f
        fi
    done
}

do_install:append() {
    # Install KERNEL_IMAGETYPE for kernel-imagebootfs package
    install -m 0644 ${KERNEL_OUTPUT_DIR}/${KERNEL_IMAGETYPE} ${D}/${KERNEL_IMAGEDEST}

    install -d ${D}/${sysconfdir}/modprobe.d
    if ${@bb.utils.contains('MACHINE_FEATURES','gpu','true','false',d)}; then
        # when ACCEPT_EULA are filled
        echo "blacklist etnaviv" > ${D}/${sysconfdir}/modprobe.d/blacklist.conf
    fi
    #snd dependencies
    install -d ${D}/${sysconfdir}/modprobe.d/
    echo "softdep snd-soc-cs42l51 pre: snd-soc-cs42l51-i2c" > ${D}/${sysconfdir}/modprobe.d/stm32mp1-snd.conf

    # efi
    if ${@bb.utils.contains('MACHINE_FEATURES','efi','true','false',d)}; then
        install -d ${D}${EFI_FILES_PATH}
        install -m 0644 ${KERNEL_OUTPUT_DIR}/${KERNEL_IMAGETYPE} ${D}${EFI_FILES_PATH}/${EFI_BOOT_IMAGE}
        install -d ${D}${EFI_PREFIX}/dtb
        install -m 0664 ${D}${EFI_PREFIX}/*.dtb ${D}${EFI_PREFIX}/dtb/
    fi

    # correct buildpaths qa
    correct_buildpaths

    # install header for sdk
    oe_runmake headers_install INSTALL_HDR_PATH=${D}${prefix}/src/linux-header-${LINUX_VERSION}
    # Kernel should not be exporting this header
    rm -f ${D}${prefix}/src/linux-header-${LINUX_VERSION}/include/scsi/scsi.h
    # The ..install.cmd conflicts between various configure runs
    find ${D}${prefix}/src/linux-header-${LINUX_VERSION} -name ..install.cmd | xargs rm -f
}

# ---------------------------------------------------------------------
do_deploy:append() {
    if [ ${MODULE_TARBALL_DEPLOY} = "1" ] && (grep -q -i -e '^CONFIG_MODULES=y$' .config); then
        mkdir -p ${D}${root_prefix}/lib
        tar -cvzf $deployDir/modules-stripped-${MODULE_TARBALL_NAME}.tgz --exclude='.debug' -C ${WORKDIR}/package/${root_prefix} lib
        ln -sf modules-stripped-${MODULE_TARBALL_NAME}.tgz $deployDir/modules-stripped-${MODULE_TARBALL_LINK_NAME}.tgz
    fi
    if [ ${KERNEL_CONFIG_DEPLOY} = "1" ] && [ -f "${D}/boot/config-${KERNEL_VERSION}" ]; then
        install -m 0644 ${D}/boot/config-${KERNEL_VERSION} $deployDir
    fi
}
do_deploy[depends] += " virtual/kernel:do_package "

# -------------------------------------------------------------
# Kernel Args
#
KERNEL_EXTRA_ARGS += "LOADADDR=${ST_KERNEL_LOADADDR}"


# -------------------------------------------------------------
# Kernel packaging
#
PACKAGES =+ "${KERNEL_PACKAGE_NAME}-headers ${KERNEL_PACKAGE_NAME}-imagebootfs"
FILES:${KERNEL_PACKAGE_NAME}-headers = "${exec_prefix}/src/linux*"
FILES:${KERNEL_PACKAGE_NAME}-imagebootfs = "boot/*.dtb boot/${KERNEL_IMAGETYPE}"
FILES:${KERNEL_PACKAGE_NAME}-modules += "${sysconfdir}/modprobe.d"

PACKAGES =+ "${@bb.utils.contains('MACHINE_FEATURES', 'efi', '${KERNEL_PACKAGE_NAME}-imageefi', '', d)} "
FILES:${KERNEL_PACKAGE_NAME}-imageefi = "${EFI_PREFIX}/dtb/ ${EFI_FILES_PATH}"
