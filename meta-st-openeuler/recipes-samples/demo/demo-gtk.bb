# Copyright (C) 2018, STMicroelectronics - All Rights Reserved

SUMMARY = "Python script which launch several use-cases"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = " \
    file://demo_launcher.py \
    file://pictures \
    file://application \
    file://board \
    file://launch-demo-gtk.sh \
    file://audio.sh \
    "

PV = "2.2"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${prefix}/local/demo/
    install -d ${D}${prefix}/local/demo/bin
    install -d ${D}${prefix}/local/demo/pictures
    install -d ${D}${prefix}/local/demo/media
    install -d ${D}${prefix}/local/demo/application
    install -d ${D}${prefix}/local/demo/board

    install -m 0755 ${WORKDIR}/demo_launcher.py ${D}${prefix}/local/demo/
    install -m 0755 ${WORKDIR}/launch-demo-gtk.sh ${D}${prefix}/local/demo/

    LIST=$(ls ${WORKDIR}/pictures/*)
    if [ -n "$LIST" ]; then
        install -m 0644 ${WORKDIR}/pictures/* ${D}${prefix}/local/demo/pictures/
    fi
    LIST=$(ls ${WORKDIR}/application/*)
    if [ -n "$LIST" ]; then
        cp -r ${WORKDIR}/application/* ${D}${prefix}/local/demo/application/
    fi
    LIST=$(ls ${WORKDIR}/board/*)
    if [ -n "$LIST" ]; then
        cp -r ${WORKDIR}/board/* ${D}${prefix}/local/demo/board/
    fi

    # start at startup
    install -d ${D}${prefix}/local/weston-start-at-startup/
    install -m 0755 ${WORKDIR}/audio.sh ${D}${prefix}/local/weston-start-at-startup/
}

FILES:${PN} += "${prefix}/local/demo ${prefix}/local/weston-start-at-startup/"

RDEPENDS:${PN} += "python3-pygobject gtk+3 python3-resource python3-threading pulseaudio-profile"
#since zeus
RDEPENDS:${PN} += " python3-core "
