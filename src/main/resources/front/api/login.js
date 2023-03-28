function loginApi(data) {
    return $axios({
      'url': '/user/login',
      'method': 'post',
      data
    })
  }
//发送短信的方法
function sendMsgApi(data){
    return $axios({
        'url':'/user/sendMsg',
        'method':'post',
        data
    })
}

function loginoutApi() {
  return $axios({
    'url': '/user/loginout',
    'method': 'post',
  })

}
