IMAGE_INSTALL += " \
    systemd-mount-partitions \
    rng-tools \
    ldd \
    e2fsprogs-resize2fs \
    myir-tool \
    can-utils \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'installer-gtk packagegroup-gstreamer1-0 v4l-utils libdrm-tests ', '', d)} \
    "
