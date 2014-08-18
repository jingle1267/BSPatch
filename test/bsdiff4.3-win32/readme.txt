需要注意的是增量升级的补丁包,是需要在服务器端,即PC端完成,大致流程如,制作补丁时调用bsdiff函数，根据两个不同版本的二进制文件，生成补丁文件。
   bsdiff的命令格式为：
   bsdiff oldfile newfile patchfile
例如: bsdiff xx_v1.0.apk xx_v2.0.apk xx.patch

补丁合成的bspatch可以通过将bspatch源码稍作修改，封装成一个so库，供手机端调用
   bspatch的命令格式为：
   bspatch oldfile newfile patchfile