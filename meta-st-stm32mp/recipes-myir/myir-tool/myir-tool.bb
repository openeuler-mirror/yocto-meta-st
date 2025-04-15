DESCRIPTION = "myir tool and wifi firmware"
LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=309cc7bace8769cfabdd34577f654f8e"

SRC_URI += " \
		file://etc/myir_test/ \
		file://etc/myir-hostapd.conf \
		file://etc/myir-udhcpd.conf \
		file://bcmd  \
		file://usr/bin/ \
		file://lib/firmware/brcm  \
		file://LICENSE \
"
S="${WORKDIR}"

inherit  systemd

do_install() {
	install -d ${D}${bindir}
	install -d ${D}${nonarch_base_libdir}/firmware/bcmd/
	install -d ${D}${nonarch_base_libdir}/firmware/brcm/
	install -d ${D}/etc/myir_test/
	install -d ${D}/etc/

	install -m 755 ${S}${bindir}/* ${D}/${bindir}/
	install -m 755 ${S}/etc/myir_test/* ${D}/etc/myir_test/ 
	install -m 755 ${S}/etc/myir-hostapd.conf ${D}/etc/myir-hostapd.conf 
	install -m 755 ${S}/etc/myir-udhcpd.conf ${D}/etc/myir-udhcpd.conf
	install -m 0644 ${S}/bcmd/* ${D}${nonarch_base_libdir}/firmware/bcmd/ 
	install -m 0644 ${S}/bcmd/BCM4345C5_003.006.006.1043.1093.hcd ${D}${nonarch_base_libdir}/firmware/brcm/
	install -m 0644 ${S}/lib/firmware/brcm/* ${D}${nonarch_base_libdir}/firmware/brcm/ 

}

FILES:${PN} = " \
	${bindir} \
	${nonarch_base_libdir}/firmware/bcmd/  \
	${nonarch_base_libdir}/firmware/brcm/  \
	/etc/myir_test/ \
	/etc/ \
"
FILES_${PN}-dbg += "${libdir}/.debug"
INSANE_SKIP_${PN} = "ldflags"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
INSANE_SKIP_${PN} = "${ERROR_QA} ${WARN_QA}"
INSANE_SKIP:${PN} = "file-rdeps"
