console.log("reply.js...");

var replyService = (function() {

    function add(reply, callback, error) {
        console.log("add reply......");

        $.ajax({
            type: 'post',
            url: '/replies/new',
            data: JSON.stringify(reply),
            contentType: 'application/json; charset=utf-8',
            success: function(result, status, xhr) {
                if (callback) {
                    callback(result);
                }
            },
            error: function(xhr, status, err) {
                if (error) {
                    error(err);
                }
            }
        });

    }

    function getList(param, callback, error) {
        console.log("get reply list......");

        var bno = param.bno;
        var page = param.page || 1;

        $.getJSON(
            `/replies/pages/${bno}/${page}`,
            function(data) {
                if (callback) {
                    callback(data.replyCount, data.list);
                }
            }
        ).fail(function(xhr, status, err) {
            if (error) {
                error();
            }
        });
    }

    function remove(rno, replyer, callback, error) {
        console.log("remove reply.... rno = " + rno);
        console.log("replyer=" + replyer);

        $.ajax({
            type: 'delete',
            url: `/replies/${rno}`,
            data: JSON.stringify({ rno: rno, replyer: replyer }),
            contentType: "application/json; charset=utf-8",
            success: function(deleteResult, status, xhr) {
                if (callback) {
                    callback(deleteResult);
                }
            },
            error: function(xhr, status, err) {
                if (error) {
                    error(err);
                }
            }
        })
    }

    function update(reply, callback, error) {
        console.log("update reply.... rno = " + reply.rno);

        $.ajax({
            type: 'put',
            url: `/replies/${reply.rno}`,
            data: JSON.stringify(reply),
            contentType: "application/json; charset=utf-8",
            success: function(result, status, xhr) {
                if (callback) {
                    callback(result);
                }
            },
            error: function(xhr, status, err) {
                if (error) {
                    error(err);
                }
            }
        })
    }

    function get(rno, callback, error) {
        console.log("get reply .... rno= " + rno);

        $.getJSON(
            `/replies/${rno}`,
            function(data) {
                if (callback) {
                    callback(data);
                }
            }
        ).fail(function(xhr, status, err) {
            if (error) {
                error();
            }
        });
    }

    function displayTime(timeValue) {

        const now = new Date();
        const nowYear = now.getFullYear();
        const nowMonth = now.getMonth() + 1;
        const nowDay = now.getDate();

        const replyYear = timeValue.year;
        const replyMonth = timeValue.monthValue;
        const replyDay = timeValue.dayOfMonth;

        const nowYearMonthDay = nowYear + nowMonth + nowDay;
        const replyYearMonthDay = replyYear + replyMonth + replyDay;

        let timeToDisplay = "";
        if (nowYearMonthDay === replyYearMonthDay) {
            timeToDisplay = `${timeValue.hour}:${timeValue.minute}:${timeValue.second}`;
        } else {
            timeToDisplay = `${replyYear}/${replyMonth}/${replyDay}`;
        }

        return timeToDisplay;
    }

    return {
        add: add,
        getList: getList,
        remove: remove,
        update: update,
        get: get,
        displayTime: displayTime
    };

})();