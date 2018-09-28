export const util = {
  methods: {
    indexColor: function (index) {
      let tagColors = ['red500', 'blue500', 'lightGreenA700', 'deepOrange500', 'brown500', 'green700', 'deepPurple800', 'purple500', 'amber500']
      return tagColors[index % tagColors.length]
    },
    formatDate: function (timeMill) {
      let date = new Date(timeMill)

      let year = date.getFullYear()
      let month = date.getMonth() + 1
      let day = date.getDate()
      let hour = date.getHours()
      let min = date.getMinutes()
      let preArr = Array.apply(null, Array(10)).map(function (elem, index) {
        return '0' + index
      })

      let newTime = year + '-' +
        (preArr[month] || month) + '-' +
        (preArr[day] || day) + ' ' +
        (preArr[hour] || hour) + ':' +
        (preArr[min] || min)
      return newTime
    },
    formatAgo: function (timeMill, dateStr, ago, justNow) {
      let mistiming = Math.round(new Date().getTime() - timeMill) / 1000
      let dataArr = dateStr.split(',')
      let arrn = [31536000, 2592000, 86400, 3600, 60]
      for (let i = 0; i < 5; i++) {
        let inm = Math.floor(mistiming / arrn[i])
        if (inm !== 0) {
          return inm + dataArr[i] + ago
        }
      }
      return justNow
    }
  }
}
