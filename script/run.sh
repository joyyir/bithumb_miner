CONNECT_KEY=your-connect-key
SECRET_KEY=your-secret-key

BASEDIR=$(dirname $0)
java -jar ${BASEDIR}/bithumb_miner-0.0.1-SNAPSHOT.jar --connect-key=${CONNECT_KEY} --secret-key=${SECRET_KEY}
