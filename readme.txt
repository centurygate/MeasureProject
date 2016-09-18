svn commit -m $"1. 修改发送的自己数据结尾标识符号为/r/n，原来的结尾标识符号是\n代码生成的System.getProperty("line.separator")会根据操作系统平台的不同得出不同的结果(Windows:换行符为/r/n; Unix|Linux : 换行符为\n/n)\n2.修改MeasureServerHandlerInitializer 中initChanenl pipeline.addLast
"

