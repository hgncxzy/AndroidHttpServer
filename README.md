# AndroidHttpServer
将 Android 设备作为服务器，并执行 get 和 post 请求
 ### 单 Android 设备作为服务器使用
单独执行 AndroidHttpServer 程序，也就是将 Android 设备作为 HttpServer 使用（意味着只有一台 Android 设备）
此时，Android 设备作为单独的服务器使用，请求本地接口即可，端口可自定义。
### 多 Android 设备局域网通信
需要同时启动 AndroidHttpServer 和 AndroidHttpClient 程序，并按照 AndroidHttpClient 程序中的步骤执行相关端口映射操作，
即可在模拟器之间调试或者在真机之间调试。具体步骤请参考[这里](https://github.com/hgncxzy/AndroidHttpClient)
### 参考文章
1. [Android上运行Http Server](https://blog.csdn.net/TurkeyCock/article/details/86555919)
