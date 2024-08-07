# jdk8
# export JAVA_HOME=
# export JRE_HOME=

wget -O alinenso-upload-qiniu-tools.jar http://www.linuxde.net/download.aspx?id=1080
# 下载上传文件

accessKey="Key"
secretKey="Sec"
spaceBucket="bucket"
domain="访问域名"
localFolder="本地文件路径"
remoteFolder="远程目录"
overrideUpload=true
refresh=true

java -jar alinenso-upload-qiniu-tools.jar \
    $accessKey \
    $secretKey \
    $spaceBucket \
    $domain \
    $localFolder \
    $remoteFolder \
    $overrideUpload \
    $refresh
