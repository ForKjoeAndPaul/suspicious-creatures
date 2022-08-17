$(function(){

    const appendDoing= function(data){
        var doingCode = '<a href="#" class="doing-link" data-id="' +
            data.id + '">' + data.name + '</a><br>';
        $('#doing-list')
            .append('<div>' + doingCode + '</div>');
    };

    //Show adding doing form
    $('#show-add-doing-form').click(function(){
        $('#doing-form').css('display', 'flex');
    });

    //Closing adding doing form
    $('#doing-form').click(function(event){
        if(event.target === this) {
            $(this).css('display', 'none');
        }
    });

    //Getting doing
    $(document).on('click', '.doing-link', function(){
        var link = $(this);
        var doingId = link.data('id');
        $.ajax({
            method: "GET",
            url: '/doings/' + doingId,
            success: function(response)
            {
                var code = '<span>Описание:' + response.description + '</span>';
                link.parent().append(code);
            },
            error: function(response)
            {
                if(response.status == 404) {
                    alert('Дело не найдено!');
                }
            }
        });
        return false;
    });

    //Adding doing
    $('#save-doing').click(function()
    {
        var data = $('#doing-form form').serialize();
        $.ajax({
            method: "POST",
            url: '/doings/',
            data: data,
            success: function(response)
            {
                $('#doing-form').css('display', 'none');
                var doing = {};
                doing.id = response;
                var dataArray = $('#doing-form form').serializeArray();
                for(i in dataArray) {
                    book[dataArray[i]['name']] = dataArray[i]['value'];
                }
                appendDoing(book);
            }
        });
        return false;
    });

});