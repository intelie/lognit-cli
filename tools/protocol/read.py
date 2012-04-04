import sys
import gzip
import StringIO
import message_bag_pb2

MAGIC_STRING = ''.join(map(chr, (32, 11, 23, 40, 3, 8, 0, 79)))

def read_bag(filename):
    content = open(filename, 'r').read()
    
    if content[0:8] == MAGIC_STRING:
        content = gzip.GzipFile(fileobj = StringIO.StringIO(content[8:])).read()

    bag = message_bag_pb2.Bag()
    bag.ParseFromString(content)
    return bag


bag = read_bag(sys.argv[1])
for message in bag.messages:
    print message.message

    for data in message.data:
        print '  ', data.key, '->', data.value


