from os import listdir
from os.path import isfile, join
import subprocess
import sys

parserFolder = "parser"

parserTestFiles = [f for f in listdir(parserFolder) if isfile(join(parserFolder, f))]

for parserTestFile in parserTestFiles:
    cmd = 'java -jar skeleton/dist/Compiler.jar -target parser parser/' + parserTestFile
    output = subprocess.check_output(cmd, stderr=subprocess.STDOUT, shell=True)
    if parserTestFile.startswith("illegal") and 'line' in output.decode(sys.stdout.encoding):
        pass
    elif parserTestFile.startswith("legal") and 'line' not in output.decode(sys.stdout.encoding):
        pass
    else:
        print("FAILED TEST: " + parserTestFile)
        exit(1)

print("All tests passed")