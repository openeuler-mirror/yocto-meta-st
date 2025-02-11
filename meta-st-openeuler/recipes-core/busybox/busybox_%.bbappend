FILESEXTRAPATHS:append := ":${THISDIR}/${PN}"

SRC_URI:append = " file://busybox-openstlinux.cfg \
		   file://0003-supports-Chinese-encoding.patch \
                "
