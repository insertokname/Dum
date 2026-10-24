{ lib, stdenv, gradle_8, makeWrapper, jre
, libGL, libpulseaudio, glfw, wayland, libxkbcommon, xorg }:

let
  gradle = gradle_8;
  runtimeLibs = lib.makeLibraryPath [
    libGL libpulseaudio glfw wayland libxkbcommon
    xorg.libX11 xorg.libXcursor xorg.libXext xorg.libXrandr xorg.libXxf86vm
  ];
in
stdenv.mkDerivation (finalAttrs: {
  pname = "mygame";
  version = "1.0.0";
  src = ./.;

  nativeBuildInputs = [ gradle makeWrapper ];

  mitmCache = gradle.fetchDeps {
    pkg = finalAttrs.finalPackage;
    data = ./deps.json;
  };

  gradleBuildTask = "lwjgl3:jar";

  installPhase = ''
    runHook preInstall
    install -Dm644 lwjgl3/build/libs/*.jar $out/share/mygame.jar
    makeWrapper ${jre}/bin/java $out/bin/mygame \
      --prefix LD_LIBRARY_PATH : ${runtimeLibs} \
      --add-flags "-jar $out/share/mygame.jar"
    runHook postInstall
  '';

  passthru = { inherit runtimeLibs; };
  meta.mainProgram = "mygame";
})
