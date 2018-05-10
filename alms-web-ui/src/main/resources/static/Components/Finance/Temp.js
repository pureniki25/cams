;(function(win,Vue){
    'use strict';
    let Temp = Vue.extend({
        template:`
            <Modal :v-model="true" title="zhe shi biao ti " ></Modal>
        `
    })
    Vue.component('Temp', Temp);
})(window, Vue);