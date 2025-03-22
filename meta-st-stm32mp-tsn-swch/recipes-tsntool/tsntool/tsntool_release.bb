SUMMARY = "TSN configuration tool"
SECTION = "tsn-base"
LICENSE = "TTTECH-license"

SRC_URI = "${TSN_SRC_URI}"
SRCREV = "${TSN_SRCREV}"

TTTECH_DIR = "tsn_sw_base.tsntool"
S = "${WORKDIR}/git/${TTTECH_DIR}"

SRC_URI += "file://LICENSE"

PV = "st-1.6.8"

EXTRA_OEMAKE = "-e "

DEPENDS = "coreutils-native libbsd"

do_compile(){
    echo ${STAGING_INCDIR}
    oe_runmake clean
    ${STAGING_BINDIR_NATIVE}/sync
    oe_runmake all
}

do_install(){
    rm -rf ${D}
    oe_runmake install DESTDIR=${D}
}

PACKAGES += "libtsn libtsn-dev"

FILES:${PN} = "${bindir}/tsntool ${mandir}/man8/tsntool.8"

FILES:libtsn = "${libdir}/lib*.so.*"

FILES:libtsn-dev = "${includedir}/libtsn/*.h ${libdir}/lib*.so"

FILES:libtsn-staticdev = "${libdir}/libtsn.a"

FILES:${PN}-dev = ""

PROVIDES = "tsntool libtsn"
LEAD_SONAME_libtsn = "libtsn.so"

# get rids of new yocto qa error
TARGET_CC_ARCH += "${LDFLAGS}"
