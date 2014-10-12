#!/bin/sh
for file in `find *.sql`
do
echo 'Executing' $file'...';
`mysql -u root < $file`
done