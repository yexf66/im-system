import json
import threading
import time

import login


# 测试心跳脚本
def doPing(scoket):
    ## 基础数据
    command = 0x270f
    messageType = login.messageType
    version = login.version
    clientType = login.clientType
    appId = login.appId
    imei = login.imei
    ## 数据转换为bytes
    commandByte = command.to_bytes(4, 'big')
    versionByte = version.to_bytes(4, 'big')
    messageTypeByte = messageType.to_bytes(4, 'big')
    appIdByte = appId.to_bytes(4, 'big')
    clientTypeByte = clientType.to_bytes(4, 'big')
    imeiBytes = bytes(imei, "utf-8")
    imeiLength = len(imeiBytes)
    imeiLengthByte = imeiLength.to_bytes(4, 'big')
    data = {}
    jsonData = json.dumps(data)
    body = bytes(jsonData, 'utf-8')
    body_len = len(body)
    bodyLenBytes = body_len.to_bytes(4, 'big')
    scoket.sendall(
        commandByte + versionByte + clientTypeByte + messageTypeByte + appIdByte + imeiLengthByte + bodyLenBytes
        + imeiBytes + body)


def ping(scoket):
    i = 0
    while True:
        time.sleep(10)
        print(i)
        i = i + 1
        doPing(scoket)


if __name__ == '__main__':
    s = login.doLogin()
    # 创建一个线程专门接收服务端数据并且打印
    t1 = threading.Thread(target=ping, args=(s,))
    t1.start()
