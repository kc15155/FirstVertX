My Vert.X Project

No external libraries were used - only vertx imports (json, etc.)

In order to compile:

mvn package

and then:

mvn org.codehaus.mojo:exec-maven-plugin:exec -Dexec.executable=java -Dexec.args="-cp %classpath io.vertx.core.Launcher run io.vertx.project.Server"

In order to run:

curl http://localhost:8080

To view current words:

curl http://localhost:8080/words

To analyze new word:

curl http://localhost:8080/analyze -d '{"text":"YOURTEXTHERE"}'

Enjoy!