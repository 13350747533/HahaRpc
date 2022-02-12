# HahaRpc
hahaRpc
##### How To Use?
### premise
Both the service provider and the service provider have the same implementation,And configure the port number and address
![image](https://user-images.githubusercontent.com/46241352/153714510-d833be80-8a07-4d07-baef-6973055567c1.png)

### first
The service provider uses annotations "@RpcService" to mark the classes to be exposed
![image](https://user-images.githubusercontent.com/46241352/153714464-7e0791a7-58c1-4427-bda4-962600c282a1.png)

### second
The service consumer gets the bean from the spring container and calls the Create method to get the instance
![image](https://user-images.githubusercontent.com/46241352/153714403-1dd6d21b-9f7d-4112-b8d9-a6c6fa80f2b0.png)

### third
Call the corresponding method
![image](https://user-images.githubusercontent.com/46241352/153714394-d3905788-d0ef-4ea0-af44-eccbaa7e39e6.png)

if you want to know more : https://editor.csdn.net/md/?articleId=122901423

12.24
修改了一些导致无法正常运行的BUG，添加了一些测试用例。

12.27
修改了server端并发处理的代码，修改了自定义的线程池。
