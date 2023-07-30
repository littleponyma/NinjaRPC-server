import requests
import json

url = "http://101.42.250.157:5888/aweme/api/v1/sign6auth"

payload = json.dumps({
  "url": "https://api5-normal-c-lf.amemv.com/passport/open/auth/?iid=618382549874461&device_id=3875153419465807&ac=wifi&channel=shenmasem_ls_dy_224&aid=1128&app_name=aweme&version_code=230300&version_name=23.3.0&device_platform=android&os=android&ssmix=a&device_type=Pixel+3+XL&device_brand=google&language=zh&os_api=30&os_version=11&manifest_version_code=230301&resolution=1440*2621&dpi=560&update_version_code=23309900&_rticket=1688648030477&package=com.ss.android.ugc.aweme&cpu_support64=true&host_abi=armeabi-v7a&is_guest_mode=0&app_type=normal&minor_status=0&appTheme=light&need_personal_recommend=1&is_android_pad=0&ts=1688648067&cdid=079a06d5-3907-45a3-b47f-0d2f93d847c6",
  "did": "3875153419465807",
  "iid": "618382549874461",
  "header": {
    "x-bd-client-key": "afc2a1a501b4208afe687efac6e97ead9b0055f79f55ce549e5b8763ce48223c84609486196db2930052c33d23c14e2f7ff21463cb983c77898fe100b34a3cd8",
    "x-tt-token": "00aa9145a35bd8180496262a6dfef07c170631c76d89710de9cd6c89a60c636da34e89c028eb178d0da808e610b50a8abff69d578ce4890263b92587ed194ef974877156e219da6cbe15f5d3417cec7aa8ca729332c5999f409f32b7cc6842dd0dc72-1.0.1",
    "x-ss-stub": "AC43A20156ACA40FF151BDF560A6561B",
    "x-tt-dt": "AAASNYVWDMQ3D3C7ENEH2OBALHZP5L2RLLYRNJGW5VFOMN7C7OGQRE7TULCBQH674SHKZ6QOIFS5IB2VW5I2CGZJPPVQOABF436WMRPSZHTTUJ2RKEDDBMRZBKMYKDK5TL3SHD3L4XIJ7CCGOIFJREA"
  }
})
headers = {
  'Content-Type': 'application/json'
}

response = requests.request("POST", url, headers=headers, data=payload)

print(response.text)
