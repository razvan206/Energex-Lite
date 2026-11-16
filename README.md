This project ilustrates real concepts behind the Energy Market and how the providers offer data to the consumer on a daily basis.
In the first place i choose to structure my solution in command, domain, repository and util packages.
Command package contains 6 commands that can modify the output as : get, import, patch, start/stop import.. and a
TransformCommand class that converts the raw data from fileio into my simulation of the events found in Command blueprint (abstract class).

I used an abstract class not an interface because i needed to have an input instance and a "default" method execute that launches my SimulationClock,
to be more precise SimulationClock just turn on/off a parameter that says if import is open/closed. 

In the first place i used a tick by tick approach that i saw it was unnecesary in the end because the input had the import commands 
already mapped from 15 in 15 minutes so i redesigned my SimulationClock class. 
In domain package i have designed a class for every type of existing date : Dam, Grid, Prediction, etc... and i made another "transformer"
for the raw data from the MarketDataInput this time and created my own MarketPoints that have helper methods as setters/getters needed to
map all of them in my local storage from MemoryRepository (repository package). 

The util package contains the SimulationClock i talked about and some methods i used in TimeConversionUtils for conversion from interval to index
or from index to interval that are usefull for fast indexing in points Map found in MarketDocument or other structures i used where 
i want to find my elements by the index key or interval one. 
To work with time and dates i first created my TimeConversionUtils because i didn't reach that part in OOP Labs at university 
and i descovered it about 2 days ago when i started to combine the java.util LocalDateTime and others..

This project was really hard for me to be honest so i tried to have a deep understanding of the concepts behind like design the packages, classes
but the JSON parsing was a new part for me so i used AI to accomodate with this topic and i put a lot of questions, i used it even to 
"concatenate" my classes between each other and for some linter issues because i had a lot of them (like missing spaces or wrong indentation)
but the source code is written 100% by me and the overall design.

Sincerely,
Voica Razvan Mihai