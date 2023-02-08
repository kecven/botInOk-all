#!/bin/bash

cd /root


function start {
    ps_out=`ps -ef | grep 'web-' | grep -v 'grep' | grep -v $0`
    result=$(echo $ps_out | grep 'web-')

    if [[ "$result" != "" ]];then
        echo "Running"
    else
        rm ./nohup.out
        # nohup ./gradlew bootRun &
        nohup java -jar ./web-0.2.7.jar &
        #for create log file
        sleep .5
    fi
}



function tails {
    tail -500f ./nohup.out
}

function kill_process {
    ps -aux | grep -i 'web-' | awk {'print $2'} | xargs kill -9
}



if [[ "stop" == $1 ]]; then
    kill_process

elif [[ "rs" == $1 ]]; then
    kill_process
    sleep .5

    echo "kill process. Run next."

    start
    tails

elif [[ "restart" == $1 ]]; then
    kill_process
    sleep .5

    start

elif [[ "run" == $1 ]]; then
    start

else
    start
    tails
fi


#pg_dump -h 127.0.0.1 -U tagger -F c -f /home/andrei/Project/tagger.top/tagger.tar.gz tagger
#pg_restore -h 127.0.0.1 -U tagger -F c -d tagger /home/andrei/Project/tagger.top/tagger.tar.gz