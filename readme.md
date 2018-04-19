# DianDangHang

## 说明：

由于node_modules、target、rev太大或者没有必要，没有上传，所以需要clone后进行一些操作。


## 所需环境：

* Node
* cnpm:若没有cnpm，将下面命令中的cnpm换成npm


## 使用步骤：

1. 用Git进行Clone
```cmd
git clone https://github.com/ALISURE/DianDangHang.git
```

2. 设置gulp
```cmd
cnpm install --save-dev gulp
```

3. 安装gulp插件

```cmd
cnpm install gulp-clean-css gulp-uglify gulp-rename gulp-rev gulp-clean gulp-rev-collector gulp-file-include gulp-autoprefixer --save-dev
```

4. 编写

只需要在`src/web`中编写前端代码即可。


## 运行步骤

下面两者有什么区别？看代码gulp配置即可。

### 开发
1. 运行gulp的default任务
2. 前端代码就在`src/main/webapp/web`中,直接使用即可
3. OK

### 上线
1. 运行gulp的product任务
2. 前端代码就在`src/main/webapp/web`中，直接使用即可
3. OK
