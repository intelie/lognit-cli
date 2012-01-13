#!/bin/bash
# simple script for turning a jar with a Main-Class
# into a stand alone executable
# cat [your jar file] >> [this file]
# then chmod +x [this file]
# you can now exec [this file]
exec java -Xmx1G -jar "$0" "$@"
