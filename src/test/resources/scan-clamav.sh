#!/bin/sh

##########################################################################
# Role:                                                                  #
# Scan un single file using clamav anti-virus                            #
##########################################################################
# Args:                                                                  #
# - file to scan                                                         #
##########################################################################
# Return:                                                                #
# - 0: scan OK - no virus 
# - 1: virus found and corrected 
# - 2: virus found but not corrected   
# - 3: Fatal scan not performed 
#
# stdout : names of virus found (1 per line) if virus found ;            #
#          failure description if failure                                #
# stderr : full ouput of clamav                                          #
##########################################################################

RET_NOTVIRUS=0
RET_VIRUS_FOUND_FIXED=1
RET_VIRUS_FOUND_NOTFIXED=2
RET_FAILURE=3

# Default return code : scan NOK
RET=3


echo Starting Clamav


if [ $# -ne 1 ]; then # Argument number must be one
	echo "ERROR : $# parameter(s) provided, only one parameter is needed"

else 
	if [ ! -f  "$1" ];then # if the file wich will be scan is existing, keep going
		echo "ERROR : \"$1\" doesn't exit"
	else
                echo "PERFORMING SCAN: " "$1"
        	clamdscan -z "$1"
		RET=$? # return code of clamscan

		if [ ${RET} -eq ${RET_VIRUS_FOUND_FIXED} ] ; then 
			RET=2 # if virus found clamscan return 1; the script must return 2
		elif [ ${RET} -eq 2 ] ; then 
                	RET=3 # if scan not performed clamscan return 2; the script must return 3
		fi
	fi
fi

echo Done ClamAV

exit ${RET}
