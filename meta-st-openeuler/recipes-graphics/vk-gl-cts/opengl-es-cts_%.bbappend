inherit python3native

PACKAGECONFIG = "${@bb.utils.filter('DISTRO_FEATURES', 'wayland', d)}"

DEPENDS += "wayland-native"

CTSDIR = "/usr/local/${BPN}"
