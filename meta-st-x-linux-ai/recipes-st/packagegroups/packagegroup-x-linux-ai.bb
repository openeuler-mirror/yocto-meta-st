SUMMARY = "X-LINUX-AI full components (frameworks and application samples)"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup python3-dir

PROVIDES = "${PACKAGES}"
PACKAGES = "                                 \
    packagegroup-x-linux-ai                  \
    packagegroup-x-linux-ai-demo             \
    packagegroup-x-linux-ai-tflite           \
    packagegroup-x-linux-ai-coral            \
    packagegroup-x-linux-ai-onnxruntime      \
"
PACKAGES:append:stm32mp25common = " packagegroup-x-linux-ai-npu"

# Manage to provide only demo with best performances depending on target used
RDEPENDS:packagegroup-x-linux-ai-demo:append:stm32mp1common = " packagegroup-x-linux-ai-tflite "
RDEPENDS:packagegroup-x-linux-ai-demo:append:stm32mp25common = " packagegroup-x-linux-ai-npu "

# Manage to provide all framework tools base packages with overall one
RDEPENDS:packagegroup-x-linux-ai = "         \
    packagegroup-x-linux-ai-tflite           \
    packagegroup-x-linux-ai-coral            \
    packagegroup-x-linux-ai-onnxruntime      \
"
RDEPENDS:packagegroup-x-linux-ai:append:stm32mp25common = " packagegroup-x-linux-ai-npu"

SUMMARY:packagegroup-x-linux-ai-tflite = "X-LINUX-AI TensorFlow Lite components"
RDEPENDS:packagegroup-x-linux-ai-tflite = "  \
    stai-mpu-tflite                          \
    stai-mpu-tools                           \
    ${PYTHON_PN}-stai-mpu                    \
    ${PYTHON_PN}-tensorflow-lite             \
    tensorflow-lite-tools                    \
    tensorflow-lite                          \
"

SUMMARY:packagegroup-x-linux-ai-coral = "X-LINUX-AI TensorFlow Lite Edge TPU components"
RDEPENDS:packagegroup-x-linux-ai-coral = "      \
    stai-mpu-tflite                             \
    stai-mpu-tools                              \
    ${PYTHON_PN}-stai-mpu                       \
    libedgetpu                                  \
    libcoral                                    \
    ${PYTHON_PN}-pycoral                        \
    ${PYTHON_PN}-tensorflow-lite                \
    tensorflow-lite                             \
    coral-edgetpu-benchmark                     \
"

SUMMARY:packagegroup-x-linux-ai-onnxruntime = "X-LINUX-AI ONNX Runtime components"
RDEPENDS:packagegroup-x-linux-ai-onnxruntime = " \
    stai-mpu-ort                             \
    stai-mpu-tools                           \
    ${PYTHON_PN}-stai-mpu                    \
    onnxruntime                              \
    onnxruntime-tools                        \
    ${PYTHON_PN}-onnxruntime                 \
"

SUMMARY:packagegroup-x-linux-ai-npu = "X-LINUX-AI minimum NPU components"
RDEPENDS:packagegroup-x-linux-ai-npu += "\
    stai-mpu-ovx              \
    stai-mpu-tools            \
    ${PYTHON_PN}-stai-mpu     \
    tim-vx                    \
    tim-vx-tools              \
    nbg-benchmark             \
"