import{d as o}from"./chunk-H3GX5QFY.js";import{c as d}from"./chunk-V6M4KDHX.js";import{b as r,f as e,g as s,i as n,j as i}from"./chunk-62MZOERU.js";import"./chunk-JHI3MBHO.js";var m=":host{display:block;-o-object-fit:contain;object-fit:contain}img{display:block;width:100%;height:100%;-o-object-fit:inherit;object-fit:inherit;-o-object-position:inherit;object-position:inherit}",b=m,f=class{constructor(t){r(this,t),this.ionImgWillLoad=i(this,"ionImgWillLoad",7),this.ionImgDidLoad=i(this,"ionImgDidLoad",7),this.ionError=i(this,"ionError",7),this.inheritedAttributes={},this.onLoad=()=>{this.ionImgDidLoad.emit()},this.onError=()=>{this.ionError.emit()},this.loadSrc=void 0,this.loadError=void 0,this.alt=void 0,this.src=void 0}srcChanged(){this.addIO()}componentWillLoad(){this.inheritedAttributes=o(this.el,["draggable"])}componentDidLoad(){this.addIO()}addIO(){this.src!==void 0&&(typeof window<"u"&&"IntersectionObserver"in window&&"IntersectionObserverEntry"in window&&"isIntersecting"in window.IntersectionObserverEntry.prototype?(this.removeIO(),this.io=new IntersectionObserver(t=>{t[t.length-1].isIntersecting&&(this.load(),this.removeIO())}),this.io.observe(this.el)):setTimeout(()=>this.load(),200))}load(){this.loadError=this.onError,this.loadSrc=this.src,this.ionImgWillLoad.emit()}removeIO(){this.io&&(this.io.disconnect(),this.io=void 0)}render(){let{loadSrc:t,alt:a,onLoad:h,loadError:c,inheritedAttributes:l}=this,{draggable:g}=l;return e(s,{key:"14d24d65ec8e5522192ca58035264971b1ab883b",class:d(this)},e("img",{key:"345ba155a5fdce5e66c397a599b7333d37d9cb1d",decoding:"async",src:t,alt:a,onLoad:h,onError:c,part:"image",draggable:u(g)}))}get el(){return n(this)}static get watchers(){return{src:["srcChanged"]}}},u=t=>{switch(t){case"true":return!0;case"false":return!1;default:return}};f.style=b;export{f as ion_img};
