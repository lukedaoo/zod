#!/bin/sh

createMavenProject() {
    mvn -B archetype:generate \
        -DarchetypeGroupId=com.infamous \
        -DarchetypeArtifactId=zod-maven-app \
        -DarchetypeVersion=1.0.0 \
        -DgroupId=$1 \
        -DartifactId=$2 \
        -Dversion=$3
}

mvnBuild() {
    echo "Excuting mvn clean install $@"
    mvn clean install $@
}
