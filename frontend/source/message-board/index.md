<script src="https://cdn.jsdelivr.net/npm/jquery/dist/jquery.min.js"></script>
<script>
  // 初始化 Valine 评论系统
  var valine = new Valine({
    el: '#vcomments',
    appId: 'cWOchgFDMTQL76RflmMTyZNw-gzGzoHsz', 
    appKey: DbJm2G62kceWcrLEXHXtDJCR, 
    visitor: true,
    verify: true,
    placeholder: '写点什么...',
    avatar: 'mm',  // 使用默认头像样式
  });

  // 监听评论提交事件
  valine.on('post', function (comment) {
    // 在提交前进行敏感词检测
    $.ajax({
      url: 'https://uapis.cn/api/prohibited',
      method: 'GET',
      data: { text: comment.content }, // 假设评论内容在这里
      success: function (response) {
        if (response.code === 200) {
          // 如果没有敏感词，允许提交
          layer.msg('评论发布成功！', { icon: 1 });
        } else {
          // 如果有敏感词，处理并给出提示
          const filteredText = response.text; // 获取过滤后的文本
          layer.alert('评论中包含不文明词汇，请修改后再试！', { icon: 2 });
        }
      },
      error: function () {
        layer.alert('评论发布失败，请检查网络或稍后重试。', { icon: 2 });
      }
    });
  });

  // 监听评论失败事件（如果需要）
  valine.on('error', function () {
    layer.alert('评论发布失败，请检查网络或稍后重试。', { icon: 2 });
  });
</script>
