{
  description = "A very basic flake";

  inputs = {
    nixpkgs.url = "https://channels.nixos.org/nixpkgs-unstable/nixexprs.tar.zst";
  };

  outputs = inputs: {
    packages = builtins.mapAttrs (system: pkgs: {
      default = pkgs.callPackage ./default.nix { };
    }) inputs.nixpkgs.legacyPackages;

    devShells = builtins.mapAttrs (system: pkgs: {
      default = pkgs.mkShell {
	packages = [ pkgs.jdk21 pkgs.gradle_8 ];
	LD_LIBRARY_PATH = (pkgs.callPackage ./default.nix { }).runtimeLibs;
      };
    }) inputs.nixpkgs.legacyPackages;
  };
}
