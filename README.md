# MRT简介

  MRT是一款使用java编写的对文件/文件夹进行批量重命名的工具，UI的实现采用javafx。源自<a href="https://github.com/LunarConcerto/AutoRenameToolForDoujinOnsei">Dlsite同人音声文件夹批量重命名工具</a>，MRT是其改良重制版。
  
  目前该工具已经有多样的规则定义，不仅可以处理DL音声的任务，也加入了其他的功能。
  
# 下载

   请前往发行页 <a href="https://github.com/LunarConcerto/MRT/releases">下载</a>
   
# 如何使用

   请参阅本仓库的Wiki页面 -> <a href="https://github.com/LunarConcerto/MRT/wiki/MRT%E4%BD%BF%E7%94%A8%E6%89%8B%E5%86%8C">MRT使用手册</a>

# 注意事项

+ MRT是一款使用java 17开发的基于jvm运行的桌面程序，因此在使用前您可能需要确保你的电脑中装有jdk17。或者也可以直接下载捆绑有jdk17环境的bundle包。
+ 在使用dlsite规则时，程序会到dlsite网站中爬取对应作品的数据来进行重命名。因此您需要保证电脑有畅通的网络连接，且能够进行科学上网。程序默认的代理地址为127.0.0.1:7890，如果您的代理程序采用了其他的地址/端口，请前往设置-网络进行修改。
+ 程序测试尚不充分，如果有报错或者崩溃，请联系我或提交issue。
