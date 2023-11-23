from sentence_transformers import SentenceTransformer
from qiniu import Auth, put_file
import zipfile
import os
import json

def zip_folder(folder_path, zip_name):
    # 创建一个压缩文件
    with zipfile.ZipFile(zip_name, 'w', zipfile.ZIP_DEFLATED) as zipf:
        for root, dirs, files in os.walk(folder_path):
            for file in files:
                file_path = os.path.join(root, file)
                arcname = os.path.relpath(file_path, os.path.join(folder_path, '..'))
                zipf.write(file_path, arcname=arcname)

def upload_to_qiniu(local_zip_path, access_key, secret_key, bucket_name, key_name):
    # 构建 Auth 对象
    q = Auth(access_key, secret_key)

    # 生成上传 Token，expires 设置上传凭证有效期
    token = q.upload_token(bucket_name, key_name, 3600)

    # 初始化 UploadManager
    ret, info = put_file(token, key_name, local_zip_path)
    if info.status_code == 200:
        print(f"模型已成功上传到七牛云存储桶 {bucket_name} 中，文件名：{key_name}")
        # 返回成功时的JSON数据
        return json.dumps({"code": 200, "msg": "操作成功"})
    else:
        print("上传失败")
        # 返回失败时的JSON数据
        return json.dumps({"code": info.status_code, "msg": "上传失败"})

if __name__ == "__main__":
    # 指定模型相关信息和七牛云配置信息
    #  model_name = 'moka-ai/m3e-base'
    model_name = 'moka-ai/m3e-large'
    local_model_path = 'local_model/m3e-base'
    local_zip_path = 'local_model/m3e-base.zip'
    key_name = model_name + '.zip'  # 在七牛云存储桶中的文件名

    access_key = ''  # 你的七牛云 Access Key
    secret_key = ''  # 你的七牛云 Secret Key
    bucket_name = ''  # 七牛云存储桶名称

    # 下载模型
    model = SentenceTransformer(model_name)
    model.save(local_model_path)

    # 压缩模型文件夹
    zip_folder(local_model_path, local_zip_path)

    # 上传压缩后的模型到七牛云存储桶
    result_json = upload_to_qiniu(local_zip_path, access_key, secret_key, bucket_name, key_name)
    print(result_json)

    # 删除本地压缩文件
    os.remove(local_zip_path)

