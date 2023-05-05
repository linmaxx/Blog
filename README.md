# Blog
  这个博客项目有前台和后台两套系统。两套系统的前端工程都已经提供好了，所以只需要写两套系统的后端。

​	实际上两套后端系统的很多内容是可能重复的。这里如果只是单纯的创建两个后端工程。那么就会有大量的重复代码，并且需要修改的时候也需要修改两次。这就是代码复用性不高。

​	所以需要创建多模块项目，两套系统可能都会用到的代码可以写到一个公共模块中，让前台系统和后台系统分别取依赖公共模块。