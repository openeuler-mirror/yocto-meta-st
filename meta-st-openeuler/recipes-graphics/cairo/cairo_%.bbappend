# openEuler ships a meson-based cairo recipe which only defines the xlib,
# xcb and trace PACKAGECONFIG options; the egl/glesv2 options understood by
# the upstream autotools recipe are invalid here and trigger
# invalid-packageconfig warnings.
PACKAGECONFIG = " ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'xlib xcb', '', d)} \
    "

do_install:append() {
    install -d ${D}${bindir}/
    install -m 0755 ${B}/util/cairo-trace/cairo-trace ${D}${bindir}/
}
