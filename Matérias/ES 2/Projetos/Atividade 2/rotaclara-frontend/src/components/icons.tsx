import type { SVGProps } from "react";

function Icon(props: SVGProps<SVGSVGElement>) {
  return (
    <svg
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth={1.8}
      {...props}
    />
  );
}

export function IconPainel(props: SVGProps<SVGSVGElement>) {
  return (
    <Icon {...props}>
      <rect x="3" y="3" width="7" height="9" rx="1.5" />
      <rect x="14" y="3" width="7" height="5" rx="1.5" />
      <rect x="14" y="12" width="7" height="9" rx="1.5" />
      <rect x="3" y="16" width="7" height="5" rx="1.5" />
    </Icon>
  );
}

export function IconHistorico(props: SVGProps<SVGSVGElement>) {
  return (
    <Icon {...props}>
      <circle cx="12" cy="13" r="8" />
      <path d="M12 9v4l3 2" />
      <path d="M7 3 4 6" />
    </Icon>
  );
}

export function IconRoteiros(props: SVGProps<SVGSVGElement>) {
  return (
    <Icon {...props}>
      <path d="M4 19c4-1 4-6 8-6s4 5 8 6" />
      <circle cx="4" cy="19" r="1.4" fill="currentColor" stroke="none" />
      <circle cx="20" cy="19" r="1.4" fill="currentColor" stroke="none" />
      <circle cx="12" cy="13" r="1.4" fill="currentColor" stroke="none" />
    </Icon>
  );
}

export function IconPontos(props: SVGProps<SVGSVGElement>) {
  return (
    <Icon {...props}>
      <path d="M12 21s7-6.4 7-11.5A7 7 0 0 0 5 9.5C5 14.6 12 21 12 21Z" />
      <circle cx="12" cy="9.5" r="2.3" />
    </Icon>
  );
}

export function IconRegistrar(props: SVGProps<SVGSVGElement>) {
  return (
    <Icon {...props}>
      <rect x="6" y="2.5" width="12" height="19" rx="2.4" />
      <path d="M10 19h4" />
    </Icon>
  );
}

export function IconMotoristas(props: SVGProps<SVGSVGElement>) {
  return (
    <Icon {...props}>
      <rect x="3" y="5" width="18" height="14" rx="2" />
      <circle cx="8.5" cy="11" r="2" />
      <path d="M5.5 16c.6-1.7 1.8-2.5 3-2.5s2.4.8 3 2.5" />
      <path d="M14.5 10h4M14.5 13h4" />
    </Icon>
  );
}

export function IconParametros(props: SVGProps<SVGSVGElement>) {
  return (
    <Icon {...props}>
      <path d="M4 7h9M17 7h3M4 12h3M9 12h11M4 17h13M19 17h1" />
      <circle cx="9" cy="7" r="1.8" fill="currentColor" stroke="none" />
      <circle cx="7" cy="12" r="1.8" fill="currentColor" stroke="none" />
      <circle cx="17" cy="17" r="1.8" fill="currentColor" stroke="none" />
    </Icon>
  );
}
