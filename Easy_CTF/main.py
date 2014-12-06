# Fast Math : 120
import socket
data_send="50" # number brute
while 1:
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client_socket.connect(('python.easyctf.com', 10660))
    client_socket.send(str(data_send))
    data = client_socket.recv(1024)
    client_socket.send(str(data_send))
    data = client_socket.recv(1024)
    if( "congrat" in data):
        print "Server:",data
        break
