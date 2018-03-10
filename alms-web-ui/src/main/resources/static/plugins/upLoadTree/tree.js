"use strict";

new Vue({
  el: '#tree',
  data: {
    items: [
      { message: '签署文书'	,
		child1:[
		  {message:'实体类' ,
		   child2:[
			   {message:'最高额借款抵押合同'},
			   {message:'借款确认书'}
		   ]
		  },
		  {message:'辅助类'}
	  	]
	  }
    ],
	isShow:false,
	iShow:false
	//show: true
  },
  methods:{
		toggle:function(){
			this.isShow = !this.isShow;
		},
		toggle2:function(){
			this.iShow = !this.iShow;
		}
	}	
});

