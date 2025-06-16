import "./assets/main.css";

import { createApp } from "vue";
import App from "./App.vue";
import { Button, message, Menu, Layout, Pagination, Tabs, Select, Input, DatePicker, Collapse, Table, Divider, Checkbox, CheckboxGroup } from "ant-design-vue";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min.js";

import { library } from "@fortawesome/fontawesome-svg-core";
import { fas } from "@fortawesome/free-solid-svg-icons";
import { fab } from "@fortawesome/free-brands-svg-icons";
import { far } from "@fortawesome/free-regular-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
library.add(fas, fab, far);

import router from "./router/index";

import Vue3Toasity from "vue3-toastify";
import "vue3-toastify/dist/index.css";

import { QuillEditor } from "@vueup/vue-quill";
import "@vueup/vue-quill/dist/vue-quill.snow.css";

const app = createApp(App);
app.component("font-awesome-icon", FontAwesomeIcon);
app.use(Button);
app.use(Menu);
app.use(Layout);
app.use(Pagination);
app.use(Tabs);
app.use(Select);
app.use(Input);
app.use(DatePicker);
app.use(Collapse);
app.use(Table);
app.use(Divider);


app.use(router);

app.use(Vue3Toasity, { autoClose: 3000 });
app.component("QuillEditor", QuillEditor);
app.mount("#app");

app.config.globalProperties.$message = message;
