import * as formElements from './formElements.js';

//------------------------------------------------

export const backgroundBlur = (active) => {
    if (active) {
        formElements.section_header.classList.add('blur');
        formElements.section_footer.classList.add('blur');
        formElements.section_form.classList.add('blur');
        formElements.section_additional.classList.add('blur');
    } else {
        formElements.section_header.classList.remove('blur');
        formElements.section_footer.classList.remove('blur');
        formElements.section_form.classList.remove('blur');
        formElements.section_additional.classList.remove('blur');
    }
}

export const backgroundGrayscale = (active) => {
    if (active) {
        document.querySelector('body').classList.add('grayscale');
    } else {
        document.querySelector('body').classList.remove('grayscale');
    }
}

export const backgroundOverlay = (active) => {
    if (active) {
        formElements.effect_overlay.classList.add('overlay');
    } else {
        formElements.effect_overlay.classList.remove('overlay');  
    }
}

