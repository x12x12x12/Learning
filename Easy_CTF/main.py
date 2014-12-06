# Fast Math : 120
import socket
while 1:
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client_socket.connect(('python.easyctf.com', 10660))
    client_socket.send(str("50"))
    data = client_socket.recv(1024)
    client_socket.send(str("50"))
    data = client_socket.recv(1024)
    if( "wow" in data):
        print "Server:",data
        break
