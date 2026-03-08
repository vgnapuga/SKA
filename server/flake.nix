{

  description = "[SKA] - Spring Boot dev environment";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-25.05";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs { inherit system; };
        javaVersion = 21;
        jdk = pkgs."jdk${toString javaVersion}";
      in {
        devShells.default = pkgs.mkShell {
          packages = with pkgs; [
            jdk
            maven
            docker-compose
          ];

          shellHook = ''
            export JAVA_HOME="${jdk}"

            git_branch() {
              git branch 2>/dev/null | grep '^\*' | sed 's/\* //'
            }
            export PS1='\[\e[0;36m\][\u@\h|\w]\[\e[0m\]>>> \[\e[0;33m\]$(git_branch)\[\e[0m\] \[\e[0;32m\]\$\[\e[0m\] '

            echo "Java $(java -version 2>&1 | head -1 | awk '{print $3}' | tr -d '\"')"
            echo "Maven $(mvn -v 2>/dev/null | head -1 | awk '{print $3}')"
          '';
        };
      }
    );

}
