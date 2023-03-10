import socket
import json
import uuid

# 测试用户登录脚本
imei = str(uuid.uuid1())
version = 1
clientType = 4
appId = 10000
messageType = 0x0
userId = 'lld'


def doLogin():
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect(("127.0.0.1", 9000))

    ## 基础数据
    command = 0x2328
    print(command)

    ## 数据转换为bytes
    commandByte = command.to_bytes(4, 'big')
    versionByte = version.to_bytes(4, 'big')
    messageTypeByte = messageType.to_bytes(4, 'big')
    clientTypeByte = clientType.to_bytes(4, 'big')
    appIdByte = appId.to_bytes(4, 'big')
    imeiBytes = bytes(imei, "utf-8")
    imeiLength = len(imeiBytes)
    imeiLengthByte = imeiLength.to_bytes(4, 'big')
    data = {"userId": userId}
    jsonData = json.dumps(data)
    body = bytes(jsonData, 'utf-8')
    body_len = len(body)
    bodyLenBytes = body_len.to_bytes(4, 'big')

    s.sendall(
        commandByte + versionByte + clientTypeByte + messageTypeByte + appIdByte + imeiLengthByte + bodyLenBytes
        + imeiBytes + body)
    return s


if __name__ == '__main__':
    doLogin()
# ping 相关逻辑
