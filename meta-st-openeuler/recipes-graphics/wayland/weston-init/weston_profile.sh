export XDG_RUNTIME_DIR=/run/user/`id -u weston`

export ELM_ENGINE=wayland_shm
export ECORE_EVAS_ENGINE=wayland_shm
export ECORE_EVAS_ENGINE=wayland_shm
export GDK_BACKEND=wayland
export PULSE_RUNTIME_PATH=/run/user/`id -u weston`/pulse
export USE_PLAYBIN3=1
export QT_QPA_PLATFORM=wayland

export WAYLAND_DISPLAY=wayland-0
if [ -e $XDG_RUNTIME_DIR/wayland-1 ]; then
    export WAYLAND_DISPLAY=wayland-1
fi
