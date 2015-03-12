# KaleWaterFall
用StaggeredGridView和Volley实现的有二级缓存和分页效果的瀑布流

## 项目概述
本项目是通过网络加载库volley和瀑布流框架StaggeredGridView实现的瀑布流范例，可以下拉刷新，滚动到底部后可以加载更多，一次性加载的数据数由服务器指定。  

![](https://github.com/tianzhijiexian/KaleWaterFall/blob/master/1.png?raw=true)  
![](https://github.com/tianzhijiexian/KaleWaterFall/blob/master/2.png?raw=true)  
![](https://github.com/tianzhijiexian/KaleWaterFall/blob/master/3.png?raw=true)  
![](https://github.com/tianzhijiexian/KaleWaterFall/blob/master/4.png?raw=true)  
## 项目优点  
- 有磁盘缓存和内存缓存
- 有稳定的网络请求机制
- 消除了过度绘制（最多是2次绘制）
- 内存控制在稳定的范围内
- 可以适配多种屏幕
- 图片有默认状态，正常状态，出错状态

## 项目缺点  

- volley的磁盘缓存有些问题，可能与LruDiskCache的同步锁有关，以后会更换为UIL
- 网络下载速度比较慢，可能和服务器图片有关。毕竟一张图片有200k+。
- 刷新时保留之前的数据比较少，以后可能要做个有固定长度的list来处理数据
- 因为是调用的api来获取网络图片，所以在理论上可以无限制的加载数据。以后会用固定长度的list做处理。

