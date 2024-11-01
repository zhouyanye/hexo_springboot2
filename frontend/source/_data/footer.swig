<div>
<!--添加网站运行时间-->
<span>小破站已经在风雨中度过了</span>
<span id="timeDate">载入天数...</span>
<span id="times">载入时分秒...</span>
<script>
    function createtime() {
        const startTime = '10/27/2024 00:12:55',
            start = new Date(startTime)
        let mill = new Date() - start,
            seconds = Math.floor(mill / 1000),
            mins = Math.floor(seconds / 60),
            hours = Math.floor(mins / 60),
            days = Math.floor(hours / 24)
        const time = {
            seconds: seconds - mins * 60,
            mins: mins - hours * 60,
            hours: hours - days * 24,
        }
        for (const k in time) {
            time[k] = `${time[k]}`.padStart(2, '0')
        }
        document.getElementById("timeDate").innerHTML = ` ${days} 天`
        document.getElementById("times").innerHTML = ` ${time.hours} 小时 ${time.mins} 分 ${time.seconds} 秒`
    }
    setInterval(createtime, 500)
</script>
</div>

<div>
    <!--添加总阅读次数-->
    <div>
        <span>本站总访问量</span>
        <span id="site_total_read_count">Loading...</span>
    </div>
    <script>
        async function sleep(ms = 1000) {
            return await new Promise(resolve => {
                const timer = setTimeout(() => {
                    clearTimeout(timer)
                    return resolve()
                }, ms)
            })
        }
        async function site_read_count() {
            const className = 'Counter',
                query = new AV.Query(className)
            return query.find()
                .then(_ => _.map(i => i.attributes.time))
                .then(_ => _.reduce((a, b) => a + b))
        }
        new Promise(resolve => {
            const timer = setInterval(() => {
                if (typeof AV !== "undefined") {
                    clearInterval(timer)
                    return resolve()
                }
            }, 250)
        })
            .then(() => site_read_count())
            .then(_ => {
                const s = ` ${_} 次`
                document.getElementById("site_total_read_count").innerHTML = s
            })
            .catch(e => console.error(e.message))
    </script>
</div>
