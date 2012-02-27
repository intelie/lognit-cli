#!/usr/bin/env python
# -*- coding: utf-8 -*-

import stomp
import sys, socket
import types
import logging, logging.config

# Settings
BROKER=(sys.argv[1], int(sys.argv[2]))     # Message Broker Target
CONN_SLEEP_DELAY = 3            # Time to wait after a connection was lost

#Local configuration for testing purposes
EVENT_TYPE = {'eventtype': sys.argv[3] }
DESTINATION = { 'destination': '/queue/events' }

logging.config.fileConfig("log.ini")
logger = logging.getLogger("eventsender")


def main():
    header = {}
    header.update(DESTINATION)
    header.update(EVENT_TYPE)
    connection = ConnectionAdapter(BROKER, CONN_SLEEP_DELAY) 
    for line in iter(sys.stdin.readline, ""): 
        connection.send(line, header, destination=header['destination'])
    
class ConnectionAdapter(object):
    """ Integrates with a the STOMP client API. """
    def __init__(self, broker, conn_sleep_delay):
        self.broker = broker
        self.conn_sleep_delay = conn_sleep_delay
        self.conn = self.__connect()
        
    def send(self, message, headers={}, **keyword_headers):
        try:
            self.conn.send(message, headers, **keyword_headers)
        except stomp.NotConnectedException:
            logger.error("Lost connection with '%s'." % (self.broker)) 
            time.sleep(self.conn_sleep_delay)
            # A própria API do stomp tenta se reconectar automaticamente, 
            # contudo apenas na parte TCP. 
            # É necessario verificar se o TCP está conectado para então 
            # conectar o Stomp.
            try:
                if self.conn.is_connected() : 
                    self.conn.connect()
            except:
                logger.debug("After wait... try again")
        
    def __connect(self):
        """ Attempts to connect to broker """
        connection = stomp.Connection([self.broker], '', '')
        connection.add_listener(ErrorListener(connection))
        connection.start()
        try:
            print("Trying to connect...")
            connection.connect()
            print "Connection established."
        except Exception:
            print "Couldn't connect."
            if type(sys.exc_info()[1]) == types.TupleType:
                exc = sys.exc_info()[1][1]
            else:
                exc = sys.exc_info()[1]
                logger.error('Unexpected error %s.' % (exc))
                return connection
        else:
            return connection
    
    
class ErrorListener(stomp.ConnectionListener):
    def __init__(self, connection):
        self.connection = connection
    
    def on_error(self, headers, message):
        logger.error('received an error %s' % message)
        if self.connection.is_connected :
            # This necessary because of an activemq bug - 
            # https://issues.apache.org/activemq/browse/AMQ-1376
            logger.error('TCP is connect but has some errors')
            self.connection.stop()


if __name__ == "__main__":
    main()

