# MRT简介

  MRT是一款使用java编写的对文件/文件夹进行批量重命名的工具，UI的实现采用javafx。源自<a href="https://github.com/LunarConcerto/AutoRenameToolForDoujinOnsei">Dlsite同人音声文件夹批量重命名工具</a>，MRT是其改良重制版，我希望在这个工具上开发出更多样的批量重命名功能，而不仅限于同人音声文件的规则(不过目前的版本还是只有同人音声的功能的)。
  
# 下载

   请前往发行页 <a href="https://github.com/LunarConcerto/MRT/releases">下载</a>
   
# 如何使用

1.  前往设置页面。
2.  设置您需要的重命名格式。
  <p align="center">
  <img src="https://github.com/LunarConcerto/MRT/blob/main/tutorial/tutorial1.png?raw=true" width="600" height="600" alt="tutorial">
</p>
3.  回到运行页面。
4.  点击选择文件夹，选择到您需要重命名的文件/文件夹所在的目录。
  <p align="center">
  <img src="https://github.com/LunarConcerto/MRT/blob/main/tutorial/tutorial2.png?raw=true" width="600" height="600" alt="tutorial">
</p>
5.  在左侧列表中点选您要进行重命名的文件/文件夹。然后在右键菜单中点击选择，这样可以使其添加到右侧待处理列表中。
6.  点击运行。程序将开始按规则进行重命名操作。
  <p align="center">
  <img src="https://github.com/LunarConcerto/MRT/blob/main/tutorial/tutorial3.png?raw=true" width="600" height="600" alt="tutorial">
</p>

# 注意事项

+ MRT是一款使用java 17开发的基于jvm运行的桌面程序，因此在使用前您可能需要确保你的电脑中装有jdk17。或者也可以直接下载捆绑有jdk17环境的bundle包。
+ 在使用dlsite规则时，程序会到dlsite网站中爬取对应作品的数据来进行重命名。因此您需要保证电脑有畅通的网络连接，且能够进行科学上网。程序默认的代理地址为127.0.0.1:7890，如果您的代理程序采用了其他的地址/端口，请前往设置-网络进行修改。
+ 程序测试尚不充分，如果有报错或者崩溃，请联系我或提交issue。
