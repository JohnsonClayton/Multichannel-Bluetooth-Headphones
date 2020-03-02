#! /bin/bash

if (( $# >= 1 )) ; then
  if [[ "$1" = "start" ]] ; then
    echo "Starting server 0"
    java -jar server/server0.jar '../music/raw_8000/STARMADE - A Synthwave Mix-MYr_WcSDBkU.raw' > server0_log &
    echo $! > server_pids
    echo "Starting server 1"
    java -jar server/server1.jar '../music/raw_8000/Khan - Vale (2018) (Full Album)-fia_b3HJbys.raw' > server1_log &
    echo $! >> server_pids
 
    multitail server0_log server1_log
  fi
 
  if [[ "$1" = "stop" ]] ; then
    pids=$(cat server_pids)
    #IFS='\n' read -ra pids_arr <<< "$(cat server_pids)"
    for pid in "${pids}"; do
      # Kill pid given
      #echo "$pid"
      kill -9 $pid
    done
  fi
fi
