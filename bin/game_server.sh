#!/bin/bash

if [ "$1"x = "-c"x ]; then
	main="com.xincao.game.server.network.AionClient"
elif [ "$1"x = "-s"x ]; then
	main="com.xincao.game.server.GameServer"
else
	echo "cmd : sh game_server.sh -[c/s] -v 1.0"
	exit 0
fi

if [ "$2"x = "-v"x ]; then
	cp ../target/game-server-"$3"-SNAPSHOT.jar ../target/game-server-"$3"-SNAPSHOT-jar-with-dependencies.jar ./
	java -Dfile.encoding=UTF-8  -classpath ./game-server-"$3"-SNAPSHOT.jar:./game-server-"$3"-SNAPSHOT-jar-with-dependencies.jar "$main"
else
	echo "cmd : sh database_server.sh -[c/s] -v 1.0"
fi
