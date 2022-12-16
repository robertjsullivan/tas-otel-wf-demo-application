#!/usr/bin/env sh

set -efux

./gradlew clean assemble

#pushd backend_app
#  cf push
#popd


BACKEND_ADDRESS=$(cf routes | grep backend | while read c1 c2 c3 c4 c5; do echo $c2.$c3; done)
pushd frontend_app
# cf push --no-start
 cf set-env frontend BACKEND_SERVICE_ADDRESS "${BACKEND_ADDRESS}"
 cf restage frontend
popd

