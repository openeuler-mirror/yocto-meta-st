SUMMARY = "GTK player with touch screen management"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "git://github.com/STMicroelectronics/st-openstlinux-application.git;protocol=https;branch=main"

# Modify these as desired
PV = "5.0+git${SRCPV}"
SRCREV = "b048c2ea1264d8f69f8cd3d1efc728975831044f"

DEPENDS += "gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-plugins-bad gtk+3"

inherit meson pkgconfig

S = "${WORKDIR}/git/touch-event-gtk-player"

do_install () {
	#install -d ${D}${prefix}/local/demo/bin
	install -d ${D}${bindir}
	#install -m 0755 ${B}/touch-event-gtk-player ${D}${prefix}/local/demo/bin/
	install -m 0755 ${B}/touch-event-gtk-player ${D}${bindir}
}
#FILES:${PN} += "${prefix}/local/demo/bin"
FILES:${PN} += "${bindir}"

