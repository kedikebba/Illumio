# ILLUMIO FLOW LOGS

## Authors
- [KEDI ](https://github.com/kedikebba)

## Description

Illumio Assessment to parse flow logs, tag it and present it.

## Project setup instructions
To start using this project use the following commands:

- `git clone https://github.com/kedikebba/Illumio.git`
- `cd Illumio`
- `code .` : # (this is if Visual Studio Code is your preferred text editor).
- Go `Main.java` class and ; 
  - Replace `flowLogDataPath` with the path to your txt file that contains the flow logs.
  - Replace `tagsDataPath` with the path to your txt file that contains the tags.
- Go to `Main.java` class and run the file. Inspect output in the console.

## Project dependencies
- `Optional` Install the JUnit and Jupiter dependencies if you want to run the tests .


## Assumptions

The following assumptions have been made prior to attempting this and the same have been used throughout the entire project. 

1. The assumed format of of the flow logs is a custom format with the following fields in their order at the respective indices. The numbers against them are their positions with `0-Indexed ` format.
```
version - 0
account-id - 1
interface-id -2
srcaddr - 3
dstaddr - 4
srcport - 5
dstport - 6
protocol - 7
packets - 8
bytes - 9
start - 10
end - 11
action - 12
log-status - 13
```
- The log entries are therefore expected to have a length of 14. 
- Entries with `len < 14` or `len > 14` will be processed into a list for manual inspection.
2. Logs with entries that input of `-` have been ignored as well since there is a high possibility they do not have enough data - [Read More Head](https://docs.aws.amazon.com/vpc/latest/userguide/flow-logs-records-examples.html)
3. Null, Empty, Blank log entries have as well been excluded. 
4. The Decimal representation of protocols has been extracted from [here](https://www.iana.org/assignments/protocol-numbers/protocol-numbers.xhtml) and any number that in the flow logs that does not have a representation here has been ingnored for translation. 
5. The tag file for mapping a tag onto the destPort + Protocol has been assumed to contain only three fields. Any length that is not equal to this will be ignored as it will seen as malfunctioned entry. And the entries will have the following fields in their order.  
```
dstport - 0,
protocol - 1,
tag - 2
```

## License info
MIT License

Copyright (c) 2018 Moringa School

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
