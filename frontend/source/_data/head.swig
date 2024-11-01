<!-- 页面点击小红心 -->
<script type="text/javascript" src="/js/love.js"></script>


<!-- 文章加密 -->
<script>
    (function(){
        if('{{ page.password }}'){
            if (prompt('请输入文章密码') !== '{{ page.password }}'){
                alert('密码错误！');
                history.back();
            }
        }
    })();
</script>

